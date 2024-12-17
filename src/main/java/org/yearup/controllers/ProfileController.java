package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;

import java.util.List;

@RestController
@RequestMapping("profile")
@CrossOrigin

public class ProfileController {
    private ProfileDao profileDao;

    @Autowired
    public ProfileController(ProfileDao profileDao){
        this.profileDao = profileDao;
    }

    @PreAuthorize("permitAll()")
    @PostMapping()
    public Profile create(@RequestBody Profile profile){
        return profileDao.create(profile);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("{userId}")
    public Profile getByUserId(@PathVariable int userId){
        Profile profile = profileDao.getByUserId(userId);

        if (profile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return profile;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("")
    public List<Profile> getAllProfile(){
        return profileDao.getAllProfiles();
    }

    @PreAuthorize("permitAll()")
    @PutMapping()
    public void update(@RequestBody Profile profile){
        profileDao.update(profile);
    }
}
