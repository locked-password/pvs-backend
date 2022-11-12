package pvs.app.members;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pvs.app.members.projects.ProjectOfResponse;
import pvs.app.members.projects.ProjectService;

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
    @ApiOperation(value = "新建使用者")
    public ResponseEntity<String> createMember(@RequestBody MemberOfCreation memberOfCreation) {
        try {
            if (null != memberService.createMember(memberOfCreation)) {
                return ResponseEntity.status(HttpStatus.OK).body("新建使用者成功");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("新建使用者失敗");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("你去死吧");
        }
    }

    @GetMapping("/members/{memberId}/projects")
    @ApiOperation(value = "取得使用者參與的所有專案")
    public ResponseEntity<List<ProjectOfResponse>> getAllProjectsByMemberId(@PathVariable Long memberId) {
        List<ProjectOfResponse> projectList = projectService.getProjectsByMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(projectList);
    }

    @GetMapping("/members/{memberId}/projects/{projectId}")
    @ApiOperation(value = "取得使用者參與的指定專案")
    public ResponseEntity<ProjectOfResponse>
    getProjectByMemberIdAndProjectId(@PathVariable Long memberId, @PathVariable Long projectId) {
        ProjectOfResponse selectedProject = projectService.getProjectsFromMemberById(projectId, memberId);
        if (selectedProject != null)
            return ResponseEntity.status(HttpStatus.OK).body(selectedProject);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
