package com.iticket.vo;

import java.util.HashMap;
import java.util.Map;

import com.iticket.Config;
import com.iticket.model.program.Program;

public class ApiProgramBeanHelp {
	public static Map<String, Object> getProgram(Program program){
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("programId", program.getId());
		resMap.put("code", program.getCode());
		resMap.put("cnName", program.getCnName());
		resMap.put("enName", program.getEnName());
		resMap.put("stadiumId", program.getStadiumId());
		resMap.put("venueId", program.getVenueId());
		resMap.put("startTime", program.getStartTime());
		
		
		resMap.put("endTime", program.getEndTime());
		resMap.put("typeId", program.getTypeId());
		resMap.put("status", program.getStatus());
		resMap.put("imgurl", Config.IMAGE_PATH + program.getImgurl());
		return resMap;
	}
}
