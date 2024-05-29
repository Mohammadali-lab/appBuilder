//package ir.samin.appbuilder.controller;
//
//import ir.samin.appbuilder.dto.AdminFullDTO;
//import ir.samin.appbuilder.dto.PageDTO;
//import ir.samin.appbuilder.dto.RoleDTO;
//import ir.samin.appbuilder.entity.PrivilegeEnum;
//import ir.samin.appbuilder.entity.RoleOrderBy;
//import ir.samin.appbuilder.entity.Sorting;
//import ir.samin.appbuilder.service.AdminService;
//import ir.samin.appbuilder.service.RoleService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.time.LocalDateTime;
//
//@RestController
//public class RoleController {
//
//
//    private AdminService adminService;
//    private RoleService roleService;
//
//    public RoleController(RoleService roleService, AdminService adminService) {
//        this.roleService = roleService;
//        this.adminService = adminService;
//    }
//
//    @PreAuthorize("hasAuthority('DETAILS_ROLES')")
//    @GetMapping("{id}")
//    public ResponseEntity<RoleDTO> getRoleDetails(@PathVariable long id) {
//        RoleDTO roleDetails = roleService.getRoleDetails(id);
//        return ResponseEntity.ok(roleDetails);
//    }
//
//    @PreAuthorize("hasAuthority('LIST_ROLES')")
//    @GetMapping
//    public ResponseEntity<PageDTO<RoleDTO>> getRoles(@RequestParam(required = false, defaultValue = "1") Integer page,
//                                                     @RequestParam(required = false, defaultValue = "10") Integer size,
//                                                     @RequestParam(required = false) Boolean isActive,
//                                                     @RequestParam(required = false) String englishName,
//                                                     @RequestParam(required = false) String persianName,
//                                                     @RequestParam(name = "createdAtDateTimeFrom", required = false) String createdAtDateTimeFrom,
//                                                     @RequestParam(name = "createdAtDateTimeTo", required = false) String createdAtDateTimeTo,
//                                                     @RequestParam(name = "createdAtDateTimeIsMatch", required = false) Boolean createdAtDateTimeIsMatch,
//                                                     @RequestParam(name = "orderBy", required = false) RoleOrderBy orderBy,
//                                                     @RequestParam(name = "sorting", required = false) Sorting sorting) {
//        LocalDateTime parseStartDate = createdAtDateTimeFrom != null ? LocalDateTime.parse(createdAtDateTimeFrom) : null;
//        LocalDateTime parseEndDate = createdAtDateTimeTo != null ? LocalDateTime.parse(createdAtDateTimeTo) : null;
//
//        if (createdAtDateTimeIsMatch != null && createdAtDateTimeIsMatch && createdAtDateTimeFrom != null) {
//            parseEndDate = parseStartDate.plusHours(24);
//        }
//        return ResponseEntity.ok(roleService.getRoles(page, size,
//                englishName, persianName, parseStartDate,
//                parseEndDate, orderBy, sorting, isActive));
//    }
//
//
//    @PreAuthorize("hasAuthority('CREATE_ROLES')")
//    @PostMapping
//    public ResponseEntity<RoleDTO> addRole(@Valid @RequestBody RoleDTO roleDTO) {
//        roleDTO = roleService.create(roleDTO);
//        return ResponseEntity.ok(roleDTO);
//    }
//
//    @PreAuthorize("hasAuthority('UPDATE_ROLES')")
//    @PostMapping("{id}")
//    public ResponseEntity<RoleDTO> updateRole(@Valid @RequestBody RoleDTO roleDTO,
//                                                                     @PathVariable long id) {
//        String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
//        roleDTO.setId(id);
//        AdminFullDTO adminByPhoneNumber = adminService.findByPhoneNumber(phoneNumber);
//        if (adminByPhoneNumber.getRole().getId() == id) {
//            throw new AccessDeniedException("You Are Not Authorized");
//        }
//        roleService.update(roleDTO);
//        return ResponseEntity.ok(roleDTO);
//    }
//
//    @PreAuthorize("hasAuthority('UPDATE_ROLES')")
//    @PostMapping("activate/{id}")
//    public ResponseEntity<RoleDTO> activateRole(@PathVariable long id) {
//        roleService.activateRole(id);
//        return ResponseEntity.ok(null);
//    }
//
//    @PreAuthorize("hasAuthority('UPDATE_ROLES')")
//    @PostMapping("deactivate/{id}")
//    public ResponseEntity<RoleDTO> deactivateRole(@PathVariable long id) {
//        roleService.deactivateRole(id);
//        return ResponseEntity.ok(null);
//    }
//
//    @PreAuthorize("hasAuthority('DELETE_ROLES')")
//    @DeleteMapping("{id}")
//    public ResponseEntity<RoleDTO> deleteRole(@PathVariable long id) {
//        roleService.deleteById(id);
//        return ResponseEntity.ok(null);
//    }
//
//    @PreAuthorize("hasAuthority('LIST_ROLES')")
//    @GetMapping("/privileges")
//    public ResponseEntity<PrivilegeEnum[]> getPrivileges() {
//        return ResponseEntity.ok(PrivilegeEnum.values());
//    }
//
//}
