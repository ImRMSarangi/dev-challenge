package com.db.awmd.challenge.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.service.AccountsService;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class TransfersControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private AccountsService accountsService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void prepareMockMvcAndCreateAccounts() {
		this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
		accountsService.getAccountsRepository().clearAccounts();
		try {
			this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
					.content("{\"accountId\":\"Id-123\",\"balance\":1000}")).andExpect(status().isCreated());
			this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
					.content("{\"accountId\":\"Id-124\",\"balance\":2000}")).andExpect(status().isCreated());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void transferMoney() throws Exception {
		this.mockMvc
				.perform(post("/v1/transfers").contentType(MediaType.APPLICATION_JSON)
						.content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-124\",\"amount\":500}"))
				.andExpect(status().isCreated());

		Account accountFrom = accountsService.getAccount("Id-123");
		Account accountTo = accountsService.getAccount("Id-124");

		assertThat(accountFrom.getBalance()).isEqualByComparingTo("500");
		assertThat(accountTo.getBalance()).isEqualByComparingTo("2500");
	}

	@Test
	public void transferMoneyNoBody() throws Exception {
		this.mockMvc.perform(post("/v1/transfers").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void transferMoneyNoAccountFromId() throws Exception {
		this.mockMvc.perform(post("/v1/transfers").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountToId\":\"Id-124\",\"amount\":500}")).andExpect(status().isBadRequest());		
	}
	
	@Test
	public void transferMoneyAccountFromIdBlank() throws Exception {
		this.mockMvc.perform(post("/v1/transfers").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountFromId\":\"\",\"accountToId\":\"Id-124\",\"amount\":500}")).andExpect(status().isBadRequest());		
	}
	
	@Test
	public void transferMoneyNoAccountToId() throws Exception {
		this.mockMvc.perform(post("/v1/transfers").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountFromId\":\"Id-123\",\"amount\":500}")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void transferMoneyAccountToIdBlank() throws Exception {
		this.mockMvc.perform(post("/v1/transfers").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"\",\"amount\":500}")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void transferMoneyNoAmount() throws Exception {
		this.mockMvc.perform(post("/v1/transfers").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-124\"}")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void transferMoneyAmountBlank() throws Exception {
		this.mockMvc.perform(post("/v1/transfers").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-124\",\"amount\":}")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void transferMoneySameFromAndToAccount() throws Exception {
		this.mockMvc.perform(post("/v1/transfers").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountFromId\":\"Id-124\",\"accountToId\":\"Id-124\",\"amount\":500}")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void transferMoneyFromAccountNotFound() throws Exception {
		this.mockMvc.perform(post("/v1/transfers").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountFromId\":\"Id-129\",\"accountToId\":\"Id-124\",\"amount\":500}")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void transferMoneyToAccountNotFound() throws Exception {
		this.mockMvc.perform(post("/v1/transfers").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-129\",\"amount\":500}")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void transferMoneyInsufficientBalance() throws Exception {
		this.mockMvc
				.perform(post("/v1/transfers").contentType(MediaType.APPLICATION_JSON)
						.content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-124\",\"amount\":5000}"))
				.andExpect(status().isBadRequest());
	}

}