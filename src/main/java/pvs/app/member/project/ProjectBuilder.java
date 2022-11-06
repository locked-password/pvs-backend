package pvs.app.member.project;

class ProjectBuilder {
    private final Project p = new Project();

    public ProjectBuilder setName(String name) {
        p.setName(name);
        return this;
    }

    public ProjectBuilder setMemberId(Long id) {
        p.setMemberId(id);
        return this;
    }

    public Project build() {
        return p;
    }
}
