package com.iticket.service;

import java.util.List;

import com.iticket.model.program.Program;

public interface ProgramService {
	List<Program> getProgramList(Long stadiumId);
}
