package com.hotelhub.config;

import com.hotelhub.models.User;

	public	class UserSession {
	    private User user;
	    // Other session specific details like timestamps

	    public UserSession(User user) {
	        this.user = user;
	    }

	    public User getUser() {
	        return user;
	    }
	}
