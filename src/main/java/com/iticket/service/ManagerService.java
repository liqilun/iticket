package com.iticket.service;

import com.iticket.model.Manager;

public interface ManagerService {
	Manager getManager(String name, String password);
}
