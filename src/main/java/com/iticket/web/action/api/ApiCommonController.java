package com.iticket.web.action.api;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iticket.Config;
import com.iticket.constant.ModuleCon;
import com.iticket.model.api.ApiUser;
import com.iticket.util.StringUtil;
import com.iticket.web.filter.OpenApiAuth;
@Controller
public class ApiCommonController extends BaseApiController{
	@RequestMapping("/inner/api/common/uploadPic.xhtml")
	public String addVenue(String picture, String fileType, ModelMap model, HttpServletRequest request) {
		OpenApiAuth auth = getOpenApiAuth(request);
		ApiUser user = auth.getApiUser();
		if(StringUtils.isBlank(picture)){
			return getErrorXmlView(model, "名称不能为空");
		}
		String fileName = "";
		try {
			byte[] x = Hex.decodeHex(picture.toCharArray());
			String ext = StringUtils.contains(fileType, ".") ? fileType : "."+fileType;
			fileName = "common/" + StringUtil.getRandomString(12).toLowerCase() + ext;
			File file = new File(Config.UPLOAD_PATH + fileName);
			FileUtils.writeByteArrayToFile(file, x);
		}  catch (Exception e) {
			e.printStackTrace();
			return getErrorXmlView(model, "处理图片异常");
		}
		dbLogger.warn(user.getId()+","+fileName);
		return getSingleResultXmlView(model, Config.IMAGE_PATH + fileName);
	}
	@RequestMapping("/inner/api/common/moduleList.xhtml")
	public String moduleList(ModelMap model, HttpServletRequest request) {
		List<Map<String, Object>> resMapList = new ArrayList<Map<String, Object>>();
		Map<String, String> moduleMap = ModuleCon.moduleMap;
		for(String module : moduleMap.keySet()){
			Map<String, Object> resMap = new HashMap<String, Object>();
			resMap.put("moduleKey", module);
			resMap.put("moduleName", moduleMap.get(module));
			resMapList.add(resMap);
		}
		return getOpenApiXmlList(resMapList, "moduleList,module", model, request);
	}
}
