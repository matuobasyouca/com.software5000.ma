package com.software5000.util;

import com.riversoft.weixin.mp.user.Tags;
import com.software5000.base.Constant;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BatchTagUtil {
    protected static Log log = LogFactory.getLog("BatchTagUtil");

    /**
     * 为用户打标签
     */
    public static void batchTagging(String openId,String type){
        try {
            if(ValidUtil.isEmpty(openId)) return;
            String superTagIds = Constant.getStringCodeValueByName(Constant.NormalSetting.SUPER_USER_TAGS);
            List<Integer> userTags = Tags.defaultTags().getUserTags(openId);//用户已有的标签
            //判断是否需要打标签
            if(ValidUtil.isNotEmpty(superTagIds)&&userTags.size()>0){
                List<String> superTags = Arrays.asList(superTagIds.trim().split(","));//数据库配置的高级标签，
                //如果用户标签已经包含了高级标签则直接返回，无需打标签
                for (Integer userTag : userTags) {
                    if(superTags.contains(userTag.toString())){
                        return;
                    }
                }
            }
            String tagIds = Constant.getStringCodeValueByName(type);//要给用户打的标签
            if (ValidUtil.isNotEmpty(tagIds)) {
                String[] split = tagIds.split(",");
                for (String s : split) {
                    Integer tagId = Integer.valueOf(s.trim());
                    if(userTags.contains(tagId)) continue;//用户已经有该标签就不打
                    List<String> list = new ArrayList();
                    list.add(openId);
                    Tags.defaultTags().tagUsers(tagId,list);
                }
            }
        }catch (Exception e){
            log.error("用户打标签失败",e);
        }
    }
}
