package cn.argento.askia.utilities;

public class FormatUtility {
    public static void main(String[] args) {
        String email1 = "example@example.com";
        String email2 = "invalid_email";
        System.out.println(matchEmail(email1));
        System.out.println(matchEmail(email2));
    }
    private static final String emailExp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static boolean matchEmail(String emailStr){
        return emailStr.matches(emailExp);
    }


    public static boolean matchPasswordLimit(String password, int limits){
        return false;
    }
}
