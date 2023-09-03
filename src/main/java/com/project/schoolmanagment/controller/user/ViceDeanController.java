package com.project.schoolmanagment.controller.user;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER', 'ASSISTANT_MANAGER')")
public class ViceDeanController {
}
