package com.project.schoolmanagment.service.user;

import com.project.schoolmanagment.entity.concretes.user.Admin;
import com.project.schoolmanagment.entity.concretes.user.UserRole;
import com.project.schoolmanagment.entity.enums.Gender;
import com.project.schoolmanagment.entity.enums.RoleType;
import com.project.schoolmanagment.payload.mappers.AdminMapper;
import com.project.schoolmanagment.payload.request.AdminRequest;
import com.project.schoolmanagment.payload.response.AdminResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.repository.user.*;
import com.project.schoolmanagment.service.validator.UniquePropertyValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ViceDeanRepository viceDeanRepository;
    @Mock
    private DeanRepository deanRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private UniquePropertyValidator uniquePropertyValidator;;
    @Mock
    private UserRoleService userRoleService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks // yukarda mocklanan nesneleri , AdminService e otomatik olarak inject islemi yapilmis oluyor
    private AdminService adminService;

    @Test
    void testSave_AdminSavedSuccessfully() {


    }

    private AdminRequest createAdminRequest(){
        AdminRequest request = new AdminRequest();
        request.setUsername("admin1");
        request.setName("John");
        request.setSurname("soyad");
        request.setPassword("12345678");
        request.setSsn("1231412121");
        request.setBirthDay(LocalDate.of(2000,1,1));
        request.setBirthPlace("City");
        request.setPhoneNumber("523725625672");
        request.setGender(Gender.MALE);
        return request;
    }

    private Admin createAdmin(){
        Admin admin = new Admin();
        admin.setUsername("admin1");
        admin.setName("John");
        admin.setSurname("soyad");
        admin.setPassword("12345678");
        admin.setSsn("1231412121");
        admin.setBirthDay(LocalDate.of(2000,1,1));
        admin.setBirthPlace("City");
        admin.setPhoneNumber("523725625672");
        admin.setGender(Gender.MALE);
        return admin;
    }



    @Test
    void deleteAdmin_Successful() {
        Long id = 1L;
        Admin admin = new Admin();
        admin.setId(id);
        admin.setBuilt_in(false);

        when(adminRepository.findById(id)).thenReturn(Optional.of(admin));

        String result =  adminService.deleteAdminById(id);
        assertEquals("Admin is deleted successfully", result);
        verify(adminRepository, times(1)).deleteById(id);
    }
}