package com.software5000.util;

import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.software5000.base.entity.ReturnResult;
import com.zscp.master.util.FileUtil;
import com.zscp.master.util.image.ImageUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

/**
 * Created by wujin on 2017/5/9.
 */
public class ImageUploadUtil {
    protected Log log = LogFactory.getLog(this.getClass());
    /**
     * 上传图片公用方法
     * @param base64Code
     * @param relativePath
     * @param rootPath
     * @return
     */
    public static String  uploadImage(String base64Code ,String relativePath,String rootPath  ) throws ServiceException {
        //图片名称
        String fileName = System.currentTimeMillis() + ((int) Math.random() * 10) + ".jpg";
        //图片相对路径
        String imgLink = relativePath+fileName;
        //图片保存路径
        String filePath = rootPath+imgLink;

        //保存图片并返回相对路径
        if (ImageUtil.generateImage(base64Code, filePath)){
            return imgLink;
        }else{
            throw new ServiceException(Constant.StateCode.IMG_ADD_ERROR.codeName);
        }
    }

    /**
     * 删除图片公用方法
     * @param  filePath
     * @return ReturnResult
     */

    public static ReturnResult deleteImage(String filePath){
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        //删除图片
        File file = new File(filePath);
        boolean flag = FileUtil.deleteFile(file);
        if(!flag){
            returnResult = ReturnResult.buildResult(Constant.StateCode.IMG_DEL_ERROR.codeName);
        }
        return  returnResult;
    }

}
