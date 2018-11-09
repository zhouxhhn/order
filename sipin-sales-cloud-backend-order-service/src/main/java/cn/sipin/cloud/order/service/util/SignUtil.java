package cn.sipin.cloud.order.service.util;

import cn.sipin.cloud.order.service.controller.ReturnOrderForErpController;
import cn.sipin.sales.cloud.order.request.returnOrder.ReturnOrderSearchRequest;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.exceptions.exception.RequestException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;

public class SignUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignUtil.class);

    public static String sign(ReturnOrderSearchRequest request, String secret) {
        StringBuilder sign = new StringBuilder(secret);
        Map map = null;
        try {
            map = ConvertUtil.objectToMap(request);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RequestException(ResponseBackCode.ERROR_AUTH_FAIL.getValue(), ResponseBackCode.ERROR_AUTH_FAIL.getMessage() + "：sign验证失败");
        }
        Map params = MapSortUtil.sortMapByKey(map);
        Iterator paramsIterator = params.entrySet().iterator();

        String key;
        while (paramsIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) paramsIterator.next();
            key = (String) entry.getKey();
            Object valueObj = entry.getValue();
            String value = null;
            if (valueObj != null) {
                value = valueObj.toString();
            }
            if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                sign.append(key).append(value);
            }
        }

        sign.append(secret);

        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        Charset charset = Charset.forName("UTF-8");

        if (charset == null) {
            throw new RuntimeException("没有找到UTF-8字符集");
        }

        byte[] bytes = sign.toString().getBytes(charset);

        md5.update(bytes);

        String hexBinary = DatatypeConverter.printHexBinary(md5.digest());

        if (hexBinary == null) {
            throw new RuntimeException("hexBinary为空");
        }
        // 注意转大写
        return hexBinary.toUpperCase();
    }
}
