package husacct.define.domain.services;

import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Module;

import java.util.ArrayList;

public class AppliedRuleExceptionDomainService {
	
	public void addExceptionToAppliedRule(long parentRuleId, String ruleType, String description, long moduleFromId, long moduleToId) {
		Module moduleFrom = SoftwareArchitecture.getInstance().getModuleById(moduleFromId);
		Module moduleTo = SoftwareArchitecture.getInstance().getModuleById(moduleToId);

		AppliedRule exceptionRule = new AppliedRule(ruleType,description, moduleFrom, moduleTo);
		
		AppliedRule parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
		parentRule.addException(exceptionRule);
	}
	
	public void removeAppliedRuleException(long parentRuleId, long exceptionRuleId) {
		AppliedRule parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
		parentRule.removeExceptionById(exceptionRuleId);
	}

	public void removeAllAppliedRuleExceptions(long appliedRuleId) {
		AppliedRule parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
		parentRule.removeAllExceptions();
	}

	public ArrayList<Long> getExceptionIdsByAppliedRule(long parentRuleId) {
		AppliedRule parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
		ArrayList<AppliedRule> exceptionRules = parentRule.getExceptions();
		ArrayList<Long> exceptionIds = new ArrayList<Long>();
		for (AppliedRule exception : exceptionRules){
			exceptionIds.add(exception.getId());
		}
		return exceptionIds;
	}
}
