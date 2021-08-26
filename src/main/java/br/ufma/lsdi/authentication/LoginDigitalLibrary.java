package br.ufma.lsdi.authentication;

import org.openqa.selenium.WebDriver;

public interface LoginDigitalLibrary {
    boolean authentication(WebDriver webDriver, String email, String password);
    boolean isStatusLogin();
}
