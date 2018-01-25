package com.db.awmd.challenge.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransfersServiceTest {

	@Autowired
	private TransfersService transfersService;
	
	@Autowired
	private NotificationService notificationService;
}
