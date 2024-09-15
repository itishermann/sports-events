package com.etna.sportevents;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Suite
@SelectClasses({
    UserControllerTest.class,
    AuthenticationControllerTest.class,
    SportControllerTest.class,
    EventControllerTest.class,
    RegistrationControllerTest.class,
    CommentControllerTest.class
})
class SportEventsApplicationTests {


}
