
package com.ucuxin.ucuxin.tec.api;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.http.volley.VolleyRequestClientAPI;

/**
 * 答题api
 *  
 * @author: sky
 */
public class AnswerAPI extends VolleyRequestClientAPI {
    
    public void submitKnowledge(RequestQueue queue, int taskid, int kpoint, String remark,String sndfile,
            final BaseActivity listener, final int requestCode) {
        //  http://www.welearn.com:8080/api/homework/hwremark
        Map<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("taskid", taskid);
        subParams.put("kpoint", kpoint);
        subParams.put("remark", remark);
        subParams.put("sndfile", sndfile);
        String dataStr = JSON.toJSONString(subParams);
        requestHttpActivity(queue, HTTP_METHOD_POST, AppConfig.GO_URL + "teacher/homeworkremark", dataStr,
                listener, requestCode);

    }
    
    
    

  

}
