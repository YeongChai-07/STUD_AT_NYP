package sg.edu.sit.ict.ippt_app.Entity;

/**
 * Created by User on 23/2/2015.
 */
public class User {

    public static String adminNo;
    public static int age;

    public static String getAdminNo() {
        return adminNo;
    }

    public static void setAdminNo(String adminNo) {
        User.adminNo = adminNo;
    }

    public static int getAge() {
        return age;
    }

    public static void setAge(int age) {
        User.age = age;
    }

}
