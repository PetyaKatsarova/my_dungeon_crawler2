package nl.hva.hvacrawler.util.security.password;

import org.springframework.stereotype.Service;

@Service
public class PepperService {

    private static final String PEPPER = "ExtraSmaak";

    public String getPEPPER() {
        return PEPPER;
    }
}
