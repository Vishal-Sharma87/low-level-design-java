package com.vishal.lld.solid.lsp.bad;

public class ContractEmployee extends Employee {

    public ContractEmployee(String name, double salary, int leaves) {
        super(name, salary, leaves);
    }

    @Override
    public int getLeaveCount() {
        throw new IllegalArgumentException("Contract Employee do not have leaves");
    }

    @Override
    public void applyLeave() {
        throw new IllegalArgumentException("Contract Employee cannot apply for leaves");
    }
}
