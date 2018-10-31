package ch.epfl.sweng.radius.utilsTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.radius.utils.UserInfos;

import static org.junit.Assert.assertTrue;

public class UserInfosTests {

    private UserInfos userInfos = new UserInfos();

    @Test
    public void testGetUserId() {
        assertTrue(userInfos.getUserId() == "1");
    }

}
