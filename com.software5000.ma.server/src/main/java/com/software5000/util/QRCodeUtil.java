package com.software5000.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.software5000.base.Constant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jiang on 2017/07/03.
 */
public class QRCodeUtil {
    protected static Log log = LogFactory.getLog(QRCodeUtil.class);

    public static void createPayQRCode(Integer payOrderId, HttpServletResponse response) {
        //生成二维码
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        // 指定编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        try {
            StringBuffer url = new StringBuffer(Constant.maUrl+Constant.getStringCodeValueByName(Constant.CashierDesk.CASHIER_DESK_URL_JSAPI));
            url.append("?payOrderId=" + payOrderId);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url.toString(), BarcodeFormat.QR_CODE, 200, 200, hints); //生成图片

            OutputStream out = response.getOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", out);//输出二维码

            out.flush();
            out.close();
        } catch (WriterException e) {
            log.error("读写失败。",e);
        } catch (IOException e) {
            log.error("添加失败。",e);
        }
    }
}
