package ma.ests.biblio.util;
import org.mindrot.jbcrypt.BCrypt;

public class HashUtil {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String candidate, String hashed) {
        if (hashed == null || !hashed.startsWith("$2a$")) return false;
        return BCrypt.checkpw(candidate, hashed);
    }
}