package team5.doghae.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import team5.doghae.common.testdata.TestUser;
import team5.doghae.domain.user.domain.User;
import team5.doghae.domain.user.repository.UserRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class BaseRepositoryTest {


    @Autowired
    protected UserRepository userRepository;

    protected User createUser() {
        return userRepository.save(TestUser.createUser());
    }

}
