package husacct.define;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.domain.Application;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Module;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.SoftwareArchitectureDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.task.ApplicationController;

import java.util.ArrayList;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

public class DefineServiceImpl implements IDefineService {
	private SoftwareArchitectureDomainService defineDomainService = new SoftwareArchitectureDomainService();
	private ModuleDomainService moduleService = new ModuleDomainService();
	private AppliedRuleDomainService appliedRuleService = new AppliedRuleDomainService();
	private DomainParser domainParser = new DomainParser();
	
	@Override
	public void createApplication(String name, String[] paths, String language, String version) {
		defineDomainService.createApplication(name, paths, language, version);
	}
	
	@Override
	public ApplicationDTO getApplicationDetails() {
		Application app = defineDomainService.getApplicationDetails();
		ApplicationDTO appDTO = domainParser.parseApplication(app);
		return appDTO;		
	}
	
	@Override 
	public ModuleDTO[] getRootModules() {	
		Module[] modules = this.moduleService.getRootModules();
		ModuleDTO[] moduleDTOs = domainParser.parseRootModules(modules);
		return moduleDTOs;
	}
	
	@Override
	public RuleDTO[] getDefinedRules() {
		AppliedRule[] rules = this.appliedRuleService.getAppliedRules();
		RuleDTO[] ruleDTOs = domainParser.parseRules(rules);
		return ruleDTOs;
	}

	@Override
	public ModuleDTO[] getChildsFromModule(String logicalPath) {
		Module module = this.moduleService.getModuleByLogicalPath(logicalPath);
		ModuleDTO moduleDTO = domainParser.parseModule(module);
		ModuleDTO[] childModuleDTOs = moduleDTO.subModules;
		
		//Removing nested childs
		for (ModuleDTO modDTO : childModuleDTOs){
			modDTO.subModules = new ModuleDTO[]{};
		}

		return childModuleDTOs;
	}

	@Override
	public String getParentFromModule(String logicalPath) {
		String parentLogicalPath = "";
		if (logicalPath.contains(".")){
			String[] moduleNames = logicalPath.split("\\.");
			parentLogicalPath += moduleNames[0];
			for (int i = 1;i<moduleNames.length-1;i++){
				parentLogicalPath += "." + moduleNames[i];
			}
			//Check if exists, an exception will automaticly be thrown
			SoftwareArchitecture.getInstance().getModuleByLogicalPath(parentLogicalPath);
		}
		else {
			parentLogicalPath = "**";
		}
		return parentLogicalPath;
	}
	
	
	public JInternalFrame getDefinedGUI(){
		ApplicationController applicationController = new ApplicationController();
		applicationController.initUi();
		JInternalFrame jinternalFrame = applicationController.getApplicationFrame();
		jinternalFrame.setVisible(false);
		return jinternalFrame;
	}
	
	public Element getLogicalArchitectureData(){
		//TODO: Implement in Construction I
		Element e = new Element("RootElement");
		return e;
	}

	public void loadLogicalArchitectureData(Element e){
		//TODO: Implement in Construction I
	}

	@Override
	public Element getWorkspaceData() {
		//TODO: Implement in Construction I
		Element e = new Element("RootElement");
		return e;
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
		//TODO: Implement in Construction I
	}

	@Override
	public boolean isDefined() {
		boolean isDefined = false;
		if (SoftwareArchitecture.getInstance().getModules().size() > 0){
			isDefined = true;
		}
		return isDefined;
	}

	@Override
	public boolean isMapped() {
		boolean isMapped = false;
		ArrayList<Module> modules = SoftwareArchitecture.getInstance().getModules();
		for (Module module : modules){	
			if (module.isMapped()){
				isMapped = true;
			}
		}
		return isMapped;
	}
}
