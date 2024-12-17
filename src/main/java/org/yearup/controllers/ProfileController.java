package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.data.ProfileDao;

@RestController
@RequestMapping
@CrossOrigin

public class ProfileController {

    private ProfileDao profileDao;

    @Autowired
    public ProfileController(ProfileDao profileDao){
        this.profileDao = profileDao;
    }


}
