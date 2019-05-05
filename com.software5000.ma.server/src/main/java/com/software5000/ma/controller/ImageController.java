package com.software5000.ma.controller;

import com.riversoft.weixin.common.WxClient;
import com.riversoft.weixin.mp.MpWxClientFactory;
import com.riversoft.weixin.mp.base.AppSetting;
import com.software5000.base.Constant;
import com.software5000.base.entity.ReturnResult;
import com.zscp.master.util.FileUtil;
import com.zscp.master.util.ValidUtil;
import com.zscp.master.util.image.ImageUtil;
import com.software5000.util.UploadFileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jinbo on 2017/3/21.
 */
@Controller
public class ImageController {

    private Log log = LogFactory.getLog(ImageController.class);

    /**
     * 上传图片
     *
     * @param paramMap
     * @param request
     * @return
     */
    @RequestMapping(value = "/home/mzscp/insertImgForBase64", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertImgForBase64(@RequestBody Map paramMap, HttpServletRequest request) {
        return generateImage(paramMap.get("img").toString(), ValidUtil.isEmpty(paramMap.get("linkType")) ? "" : paramMap.get("linkType").toString());
    }

    /**
     * 上传图片（微信素材）
     *
     * @param paramMap
     * @param request
     * @return
     */
    @RequestMapping(value = "/home/mzscp/insertImgForMediaId", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertImgForMediaId(@RequestBody Map paramMap, HttpServletRequest request) {

        //根据MediaId下载素材
        WxClient wxClient = MpWxClientFactory.getInstance().with(AppSetting.defaultSettings());
        String mediaId = paramMap.get("mediaId").toString();
        String imageStr = ImageUtil.getImageStr(wxClient.download("https://api.weixin.qq.com/cgi-bin/media/get?media_id="+mediaId).getPath());

        return generateImage(imageStr, ValidUtil.isEmpty(paramMap.get("linkType")) ? "" : paramMap.get("linkType").toString());
    }

    /**
     * 上传图片（base64）
     * @param imageStr
     * @param linkType
     * @return
     */
    private ReturnResult generateImage(String imageStr, String linkType) {
        //图片根路径
        String relativePath = Constant.getStringCodeValueByName(linkType);
        //判断是否是正确类型，如果不是，则使用默认路径
        if (ValidUtil.isEmpty(relativePath)) {
            relativePath = Constant.getStringCodeValueByName(Constant.ImagePath.DEFAULT_PATH.codeName);
        }

        //图片流
        String fileName = System.currentTimeMillis() + ((int) Math.random() * 10) + ".jpg"; //文件名称
        String rootPath = Constant.getStringCodeValueByName(Constant.ImagePath.IMAGE_SAVEROOT_PATH.codeName) + File.separator + relativePath; //存放路径
        String filePath = rootPath + File.separator + fileName; //文件完整路径

        //生成图片
        if (ImageUtil.generateImage(imageStr, filePath)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("imgLink", relativePath + fileName);
            map.put("imgSrc", Constant.imgUrl+ relativePath + fileName);
            return ReturnResult.buildSuccessMsg().setData(map);
        }
        return ReturnResult.buildEnumResult(Constant.StateCode.IMG_ADD_ERROR);
    }

    /**
     * 删除图片（单个，公用）
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/home/mzscp/deleteImg", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult deleteImg(@RequestBody Map param) {
        return ReturnResult.buildSuccessMsg().setData(deleteImage(param.get("imgLink").toString(), Constant.getStringCodeValueByName(Constant.ImagePath.IMAGE_SAVEROOT_PATH.codeName)));
    }

    /**
     * 删除图片（多个，公用）
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/home/mzscp/deleteImgList", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult deleteImgList(@RequestBody Map param) {
        return ReturnResult.buildSuccessMsg().setData(((List<String>) param.get("imgLinkList")).stream().map(s -> deleteImage(s, Constant.getStringCodeValueByName(Constant.ImagePath.IMAGE_SAVEROOT_PATH.codeName))).collect(Collectors.toList()));
    }

    /**
     * 删除图片
     *
     * @param imgPath
     * @param rootPath
     * @return
     */
    public boolean deleteImage(String imgPath, String rootPath) {

        //防止错误删除
        if(ValidUtil.isEmpty(imgPath) || ValidUtil.isEmpty(rootPath)) {
            return false;
        }

        //删除图片
        File file = new File(rootPath + imgPath);
        boolean flag = FileUtil.deleteFile(file);
        if (!flag) {
            return false;
        }
        return true;
    }

    /**
     * 添加图片（公用）
     *
     * @param linkType
     * @param request
     * @return
     */
    @RequestMapping(value = "/home/mzscp/insertImg", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertImg(@RequestParam(value = "linkType", required = false) String linkType, HttpServletRequest request) {
        //图片根路径
        String relativePath = Constant.getStringCodeValueByName(linkType);
        //判断是否是正确类型，如果不是，则使用默认路径
        if (ValidUtil.isEmpty(relativePath)) {
            relativePath = Constant.getStringCodeValueByName(Constant.ImagePath.DEFAULT_PATH.codeName);
        }
        return uploadImage(request, relativePath);
    }

    /**
     * 上传图片
     *
     * @param request
     * @param relativePath
     * @return
     */
    public ReturnResult uploadImage(HttpServletRequest request, String relativePath) {
        try {
            //图片名称
            String fileName = System.currentTimeMillis() + ((int) Math.random() * 10) + ".jpg";
            //保存图片并返回相对路径
            if (UploadFileUtil.uploadFile(request, Constant.getStringCodeValueByName(Constant.ImagePath.IMAGE_SAVEROOT_PATH.codeName) + File.separator + relativePath + File.separator, fileName)) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("imgLink", relativePath + fileName);
                map.put("imgSrc", Constant.imgUrl + relativePath + fileName);
                return ReturnResult.buildSuccessMsg().setData(map);
            }
        } catch (IOException e) {
            log.error("ImageController.uploadImage", e);
        }
        return ReturnResult.buildEnumResult(Constant.StateCode.IMG_ADD_ERROR);
    }
}
