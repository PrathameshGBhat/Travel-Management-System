package com.cts.util;
 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 
public class PasswordGenerator {
	public static void main(String[] args)
	{
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		System.out.println(encoder.encode("password123"));
		System.out.println(encoder.encode("Kartthik@123"));
		
	}
}