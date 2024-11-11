package SpringSecurityJwt.repository;

import SpringSecurityJwt.model.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureMockMvc(addFilters = false)
class IRolesRepositoryTest {
    @Autowired
    IRolesRepository iRolesRepository;
    @Autowired
    TestEntityManager testEntityManager;


    @BeforeEach
    void setUp() {
        Roles rol = new Roles();
        rol.setIdRole(1L);
        rol.setName("ADMIN");
        testEntityManager.persist(rol);
    }

    @Test
    public void findRolById(){
        Optional<Roles> testRol = iRolesRepository.findById(1L);
        assertEquals(testRol.get().getIdRole(),1L);
        System.out.println("testRol.get() = " + testRol.get().getName());
    }
}