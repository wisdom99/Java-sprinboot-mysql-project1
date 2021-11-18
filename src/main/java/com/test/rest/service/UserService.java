package com.test.rest.service;

import com.test.rest.model.User;
import com.test.rest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserService {

	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void saveUser(User user) {
		userRepository.save(user);
	}

	public String getUser(String userName) {

		String message = "";

		User retrievedUser = userRepository.findByUserName(userName).orElse(null);
		if (retrievedUser != null) {
			Date dateOfBirth = new Date();
			Date today = new Date();
			try {
				dateOfBirth = new SimpleDateFormat("MM-dd").parse(retrievedUser.getDateOfBirth().substring(5));
				String todayString = new SimpleDateFormat("MM-dd").format(new Date());
				today = new SimpleDateFormat("MM-dd").parse(todayString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long diff = today.getTime() - dateOfBirth.getTime();

			long diffIndays = diff / (1000 * 60 * 60 * 24);
//			System.out.println("Difference between  " + today + " and " + dateOfBirth + " is "
//					+ diffIndays + " days.");

			if (diffIndays == 0) {
				message = "Happy birthday";
			} else {
				message = "Your birthday is in " + diffIndays + " day(s) time";
			}
		}
		return message;
	}
}
