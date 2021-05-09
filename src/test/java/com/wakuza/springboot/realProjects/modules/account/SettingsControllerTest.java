package com.wakuza.springboot.realProjects.modules.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wakuza.springboot.realProjects.modules.account.WithAccount;
import com.wakuza.springboot.realProjects.modules.account.Account;
import com.wakuza.springboot.realProjects.modules.account.AccountRepository;
import com.wakuza.springboot.realProjects.modules.account.AccountService;
import com.wakuza.springboot.realProjects.modules.tag.Tag;
import com.wakuza.springboot.realProjects.modules.zone.Zone;
import com.wakuza.springboot.realProjects.modules.tag.TagForm;
import com.wakuza.springboot.realProjects.modules.tag.TagRepository;
import com.wakuza.springboot.realProjects.modules.zone.ZoneForm;
import com.wakuza.springboot.realProjects.modules.zone.ZoneRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.wakuza.springboot.realProjects.modules.account.SettingsController.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountRepository accountRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired ObjectMapper objectMapper;
    @Autowired TagRepository tagRepository;
    @Autowired AccountService accountService;
    @Autowired ZoneRepository zoneRepository;

    private Zone testZone = Zone.builder().city("test").localNameOfCity("테스트시").province("테스트주").build();

    @BeforeEach
    void beforeEach(){
        zoneRepository.save(testZone);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
        zoneRepository.deleteAll();
    }

    @WithAccount("gaeomna")
    @DisplayName("프로필 수정하기 - 입력값 정상")
    @Test
    void updateProfile() throws Exception {
        String bio = "짧은 소개를 수정하는 경우";
        mockMvc.perform(post(ROOT + SETTINGS + PROFILE)
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ROOT + SETTINGS + PROFILE))
                .andExpect(flash().attributeExists("message"));

        Account gaeomna = accountRepository.findByNickname("gaeomna");
        assertEquals(bio, gaeomna.getBio());
    }


    @WithAccount("gaeomna")
    @DisplayName("프로필 수정하기 - 입력값 오류")
    @Test
    void updateProfile_error() throws Exception {
        String bio = "길게 소개를 수정하는 경우, 길게 소개를 수정하는 경우, 길게 소개를 수정하는 경우, 길게 소개를 수정하는 경우";
        mockMvc.perform(post(ROOT + SETTINGS + PROFILE)
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SETTINGS + PROFILE))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account gaeomna = accountRepository.findByNickname("gaeomna");
        assertNull(gaeomna.getBio());
    }

    @WithAccount("gaeomna")
    @DisplayName("프로필 수정 폼")
    @Test
    void updateProfileForm() throws Exception {
        mockMvc.perform(get(ROOT + SETTINGS + PROFILE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }

    @WithAccount("gaeomna")
    @DisplayName("패스워드 수정 폼")
    @Test
    void updatePassword_Form() throws Exception {
        mockMvc.perform(get(ROOT + SETTINGS + PASSWORD))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"));
    }

    @WithAccount("gaeomna")
    @DisplayName("패스워드 수정 폼 - 성공")
    @Test
    void updatePassword_Success() throws Exception {
        mockMvc.perform(post(ROOT + SETTINGS + PASSWORD)
                .param("newPassword", "123123123")
                .param("newPasswordConfirm", "123123123")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ROOT + SETTINGS + PASSWORD))
                .andExpect(flash().attributeExists("message"));

        Account gaeomna = accountRepository.findByNickname("gaeomna");
        assertTrue(passwordEncoder.matches("123123123", gaeomna.getPassword()));
    }

    @WithAccount("gaeomna")
    @DisplayName("패스워드 수정 입력 오류 - 패스워드 불일치")
    @Test
    void updatePassword_Fail() throws Exception {
        mockMvc.perform(post(ROOT + SETTINGS + PASSWORD)
                .param("newPassword", "123123123")
                .param("newPasswordConfirm", "111111111")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SETTINGS + PASSWORD))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(model().attributeExists("account"));
    }

    @WithAccount("gaeomna")
    @DisplayName("알림 폼")
    @Test
    void updateNotification_Form() throws Exception {
        mockMvc.perform(get(ROOT + SETTINGS + NOTIFICATIONS))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("notifications"));
    }

    @WithAccount("gaeomna")
    @DisplayName("계정의 태그 수정 폼")
    @Test
    void updateTagsForm() throws Exception {
        mockMvc.perform(get(ROOT + SETTINGS + TAGS))
                .andExpect(view().name(SETTINGS + TAGS))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("tags"));
    }

    @WithAccount("gaeomna")
    @DisplayName("계정의 태그 추가")
    @Test
    void addTag() throws Exception {
        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post(ROOT + SETTINGS + TAGS + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm)) //content("{\"tagTitle\": \"newTag\"}");
                .with(csrf()))
                .andExpect(status().isOk());

        Tag newTag = tagRepository.findByTitle("newTag");
        assertNotNull(newTag);
        Account gaeomna = accountRepository.findByNickname("gaeomna");
        assertTrue(gaeomna.getTags().contains(newTag));
    }

    @WithAccount("gaeomna")
    @DisplayName("계정의 태그 삭제")
    @Test
    void removeTag() throws Exception {
        Account gaeomna =accountRepository.findByNickname("gaeomna");
        Tag newTag = tagRepository.save(Tag.builder().title("newTag").build());
        accountService.addTag(gaeomna,newTag);

        assertTrue(gaeomna.getTags().contains(newTag));
        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post(ROOT + SETTINGS + TAGS + "/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm)) //content("{\"tagTitle\": \"newTag\"}");
                .with(csrf()))
                .andExpect(status().isOk());

        assertFalse(gaeomna.getTags().contains(newTag));
    }
    @WithAccount("gaeomna")
    @DisplayName("계정의 지역 정보 수정 폼")
    @Test
    void updateZoneForm() throws Exception {
        mockMvc.perform(get(ROOT + SETTINGS + ZONE))
                .andExpect(view().name(SETTINGS + ZONE))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("zones"));
    }

    @WithAccount("gaeomna")
    @DisplayName("계정의 지역 정보 추가")
    @Test
    void addZone() throws Exception {
        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());

        mockMvc.perform(post(ROOT + SETTINGS + ZONE + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(zoneForm)) //content("{\"tagTitle\": \"newTag\"}");
                .with(csrf()))
                .andExpect(status().isOk());
        Account gaeomna = accountRepository.findByNickname("gaeomna");
        Zone zone = zoneRepository.findByCityAndProvince(testZone.getCity(), testZone.getProvince());
        assertTrue(gaeomna.getZones().contains(zone));
    }

    @WithAccount("gaeomna")
    @DisplayName("계정의 지역 정보 삭제")
    @Test
    void removeZone() throws Exception {
        Account gaeomna =accountRepository.findByNickname("gaeomna");
        Zone zone = zoneRepository.findByCityAndProvince(testZone.getCity(), testZone.getProvince());
        accountService.addZone(gaeomna,zone);

        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());

        mockMvc.perform(post(ROOT + SETTINGS + ZONE + "/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(zoneForm)) //content("{\"tagTitle\": \"newTag\"}");
                .with(csrf()))
                .andExpect(status().isOk());

        assertFalse(gaeomna.getZones().contains(zone));
    }





}