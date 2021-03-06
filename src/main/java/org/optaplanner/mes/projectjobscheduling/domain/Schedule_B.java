/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.mes.projectjobscheduling.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.mes.common.domain.AbstractPersistable;
import org.optaplanner.mes.projectjobscheduling.domain.resource.Resource;
import org.optaplanner.persistence.xstream.impl.score.XStreamBendableScoreConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@SuppressWarnings("serial")
@PlanningSolution
@XStreamAlias("PjsSchedule")
public class Schedule_B extends AbstractPersistable implements Solution<BendableScore> {

	private List<Project> projectList;
	private List<Job> jobList;
	private List<ExecutionMode> executionModeList;
	private List<Resource> resourceList;
	private List<ResourceRequirement> resourceRequirementList;
	private List<Allocation> allocationList;
	private int mesSchedulingId;
	private int mesTimeScale;

	@XStreamConverter(value = XStreamBendableScoreConverter.class, ints = { 1, 2, 3 })
	private BendableScore score;

	public List<Project> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Project> projectList) {
		this.projectList = projectList;
	}

	public List<Job> getJobList() {
		return jobList;
	}

	public void setJobList(List<Job> jobList) {
		this.jobList = jobList;
	}

	public List<ExecutionMode> getExecutionModeList() {
		return executionModeList;
	}

	public void setExecutionModeList(List<ExecutionMode> executionModeList) {
		this.executionModeList = executionModeList;
	}

	public List<Resource> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<Resource> resourceList) {
		this.resourceList = resourceList;
	}

	public List<ResourceRequirement> getResourceRequirementList() {
		return resourceRequirementList;
	}

	public void setResourceRequirementList(List<ResourceRequirement> resourceRequirementList) {
		this.resourceRequirementList = resourceRequirementList;
	}

	@PlanningEntityCollectionProperty
	public List<Allocation> getAllocationList() {
		return allocationList;
	}

	public void setAllocationList(List<Allocation> allocationList) {
		this.allocationList = allocationList;
	}

	public BendableScore getScore() {
		return score;
	}

	public void setScore(BendableScore score) {
		this.score = score;
	}

	public int getMesSchedulingId() {
		return mesSchedulingId;
	}

	public void setMesSchedulingId(int mesSchedulingId) {
		this.mesSchedulingId = mesSchedulingId;
	}

	public int getMesTimeScale() {
		return mesTimeScale;
	}

	public void setMesTimeScale(int mesTimeScale) {
		this.mesTimeScale = mesTimeScale;
	}

	// ************************************************************************
	// Complex methods
	// ************************************************************************

	public Collection<? extends Object> getProblemFacts() {
		List<Object> facts = new ArrayList<Object>();
		facts.addAll(projectList);
		facts.addAll(jobList);
		facts.addAll(executionModeList);
		facts.addAll(resourceList);
		facts.addAll(resourceRequirementList);
		// Do not add the planning entity's (allocationList) because that will
		// be done automatically
		return facts;
	}

}