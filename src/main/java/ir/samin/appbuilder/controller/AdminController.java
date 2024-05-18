package ir.samin.appbuilder.controller;


import ir.samin.appbuilder.service.AdminService;

public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {

        this.adminService = adminService;
    }

//    @PreAuthorize("hasAuthority('CREATE_ADMINS')")
//    @PostMapping("/create")
//    public ResponseEntity addAdmin(@Valid @RequestBody AdminCreationDTO adminCreationDTO) {
//        UserDetails userDetails = adminService.loadUserByUsername(adminCreationDTO.getEmail());
//        if (userDetails != null) {
////            adminService.addLoginHistory(adminCreationDTO, ip, false);
//            return badRequest()
//                    .body(new ApiError(null, "username exist.", "username exist."));
//        } else {
//            adminService.createAdminFullDTO(adminCreationDTO);
//
//            return ResponseEntity.ok(new SuccessfulResponseDTO<>(null, "Admin Created Successfully"));
//        }
//    }
}
