package com.javamentor.qa.platform.api;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.javamentor.qa.platform.AbstractClassForDRRiderMockMVCTests;
import com.javamentor.qa.platform.models.entity.user.User;
import org.junit.jupiter.api.Test;

import javax.persistence.Query;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestAdminResourceController extends AbstractClassForDRRiderMockMVCTests {

    private final String publicUrl = "/api/user/answer/id?page=1&items=10";
    private final String testUsername = "user1@mail.ru";
    private final String testPassword = "user1";
    private final long id = 1;

    @Test
    @DataSet(cleanBefore = true, value = "dataset/AdminResourceController/users.yml", strategy = SeedStrategy.REFRESH )
    public void shouldBanUser() throws Exception {
        String token = getToken(testUsername,testPassword);
        // Проверка того, что api изначально доступен
        this.mockMvc.perform(get(publicUrl)
                .header("Authorization", "Bearer " + token))
                .andDo(print()).andExpect(status().isOk());

        // Блокировка юзера
        this.mockMvc.perform(put("/api/admin/disable-user/" + id)
                .header("Authorization", "Bearer " + token))
                .andDo(print()).andExpect(status().isOk());

        // Проверка того, что состояние флага enable изменилось на false
        Query query = entityManager.createQuery("select u from User u where u.id=:id").setParameter("id", id);
        User user = (User) query.getSingleResult();
        assertThat(user.getIsEnabled()).isEqualTo(false);

        // Проверка того, что заблокированный юзер не может использовать api
        this.mockMvc.perform(get(publicUrl)
                .header("Authorization", "Bearer " + token))
                .andDo(print()).andExpect(status().isForbidden());
    }
}
