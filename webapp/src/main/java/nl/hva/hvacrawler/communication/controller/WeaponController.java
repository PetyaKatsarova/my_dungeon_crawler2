package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.Weapon;
import nl.hva.hvacrawler.business.service.WeaponService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class WeaponController {
    private final WeaponService weaponService;

    public WeaponController(WeaponService weaponService) {
        this.weaponService = weaponService;
    }

    @GetMapping("/weapon/{id}")
    @ResponseBody
    public Weapon getWeapon(@PathVariable int id){
        return weaponService.getWeaponById(id).orElse(null);
    }

    @GetMapping("/random-weapon")
    @ResponseBody
    public Weapon getRandomWeapon() {
        Optional<Weapon> randomWeapon = weaponService.getRandomWeapon();
        return randomWeapon.get();
    }
}
