package pvs.app.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pvs.app.member.project.ProjectOfResponse;
import pvs.app.member.project.ProjectService;

import java.util.List;

@RestController
@RequestMapping(path = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;

    private final ProjectService projectService;

    @Autowired
    public MemberController(MemberService memberService, ProjectService projectService) {
        this.memberService = memberService;
        this.projectService = projectService;
    }

    @PostMapping("/members")
    public ResponseEntity<String> createMember(@RequestBody MemberDTO memberDTO) {
        try {
            if (null != memberService.createUser(memberDTO)) {
                return ResponseEntity.status(HttpStatus.OK).body("新建使用者成功");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("新建使用者失敗");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("你去死吧");
        }
    }

    @GetMapping("/members/{memberId}/projects")
    public ResponseEntity<List<ProjectOfResponse>> readMemberAllProjects(@PathVariable Long memberId) {
        List<ProjectOfResponse> projectList = projectService.getProjectsByMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(projectList);
    }

    @GetMapping("/members/{memberId}/projects/{projectId}")
    public ResponseEntity<ProjectOfResponse> readSelectedProject(
            @PathVariable Long memberId, @PathVariable Long projectId) {
        ProjectOfResponse selectedProject = projectService.getProjectsFromMemberById(projectId, memberId);
        if (selectedProject != null)
            return ResponseEntity.status(HttpStatus.OK).body(selectedProject);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
