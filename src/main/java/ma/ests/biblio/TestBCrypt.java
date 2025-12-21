package ma.ests.biblio;

import org.mindrot.jbcrypt.BCrypt;

public class TestBCrypt {
    public static void main(String[] args) {
        String password = "user123";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(hash);
    }
}
