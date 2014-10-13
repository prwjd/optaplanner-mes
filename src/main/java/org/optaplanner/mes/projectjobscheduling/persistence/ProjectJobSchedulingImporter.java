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

package org.optaplanner.mes.projectjobscheduling.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.mes.common.persistence.AbstractTxtSolutionImporter;
import org.optaplanner.mes.projectjobscheduling.domain.Allocation;
import org.optaplanner.mes.projectjobscheduling.domain.ExecutionMode;
import org.optaplanner.mes.projectjobscheduling.domain.Job;
import org.optaplanner.mes.projectjobscheduling.domain.JobType;
import org.optaplanner.mes.projectjobscheduling.domain.Project;
import org.optaplanner.mes.projectjobscheduling.domain.ResourceRequirement;
import org.optaplanner.mes.projectjobscheduling.domain.Schedule;
import org.optaplanner.mes.projectjobscheduling.domain.resource.GlobalResource;
import org.optaplanner.mes.projectjobscheduling.domain.resource.LocalResource;
import org.optaplanner.mes.projectjobscheduling.domain.resource.Resource;

public class ProjectJobSchedulingImporter extends AbstractTxtSolutionImporter {

	public static void main(String[] args) {
		new ProjectJobSchedulingImporter().convertAll();
	}

	public ProjectJobSchedulingImporter() {
		super(new ProjectJobSchedulingDao());
	}

	public TxtInputBuilder createTxtInputBuilder() {
		return new ProjectJobSchedulingInputBuilder();
	}

	public static class ProjectJobSchedulingInputBuilder extends TxtInputBuilder {

		private Schedule schedule;

		private int projectListSize;
		private int resourceListSize;
		private int globalResourceListSize;

		private long projectId = 0L;
		private long resourceId = 0L;
		private long jobId = 0L;
		private long executionModeId = 0L;
		private long resourceRequirementId = 0L;

		private Map<Project, File> projectFileMap;

		private boolean useMesAllocations = true;

		public Solution<?> readSolution() throws IOException {
			schedule = new Schedule();
			schedule.setId(0L);
			readProjectList();
			readResourceList();
			for (Map.Entry<Project, File> entry : projectFileMap.entrySet()) {
				readProjectFile(entry.getKey(), entry.getValue());
			}
			removePointlessExecutionModes();
			if (useMesAllocations) {
				createMesAllocationList();
			} else {
				createAllocationList();
			}

			logger.info("Schedule {} has {} projects, {} jobs, {} execution modes, {} resources" + " and {} resource requirements.", getInputId(), schedule
					.getProjectList().size(), schedule.getJobList().size(), schedule.getExecutionModeList().size(), schedule.getResourceList().size(), schedule
					.getResourceRequirementList().size());
			return schedule;
		}

		private void readProjectList() throws IOException {
			projectListSize = readIntegerValue();
			List<Project> projectList = new ArrayList<Project>(projectListSize);
			projectFileMap = new LinkedHashMap<Project, File>(projectListSize);
			for (int i = 0; i < projectListSize; i++) {
				Project project = new Project();
				project.setId(projectId);
				project.setReleaseDate(readIntegerValue());
				project.setCriticalPathDuration(readIntegerValue());
				File projectFile = new File(inputFile.getParentFile(), readStringValue());
				if (!projectFile.exists()) {
					throw new IllegalArgumentException("The projectFile (" + projectFile + ") does not exist.");
				}
				projectFileMap.put(project, projectFile);
				projectList.add(project);
				projectId++;
			}
			schedule.setProjectList(projectList);
			schedule.setJobList(new ArrayList<Job>(projectListSize * 10));
			schedule.setExecutionModeList(new ArrayList<ExecutionMode>(projectListSize * 10 * 5));
		}

		private void readResourceList() throws IOException {
			resourceListSize = readIntegerValue();
			String[] tokens = splitBySpacesOrTabs(readStringValue(), resourceListSize);
			List<Resource> resourceList = new ArrayList<Resource>(resourceListSize * projectListSize * 10);
			for (int i = 0; i < resourceListSize; i++) {
				int capacity = Integer.parseInt(tokens[i]);
				if (capacity != -1) {
					GlobalResource resource = new GlobalResource();
					resource.setId(resourceId);
					resource.setCapacity(capacity);
					resourceList.add(resource);
					resourceId++;
				}
			}
			globalResourceListSize = resourceList.size();
			schedule.setResourceList(resourceList);
			schedule.setResourceRequirementList(new ArrayList<ResourceRequirement>(projectListSize * 10 * 5 * resourceListSize));
		}

		private void readProjectFile(Project project, File projectFile) {
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(projectFile), "UTF-8"));
				ProjectFileInputBuilder projectFileInputBuilder = new ProjectFileInputBuilder(schedule, project);
				projectFileInputBuilder.setInputFile(projectFile);
				projectFileInputBuilder.setBufferedReader(bufferedReader);
				try {
					projectFileInputBuilder.readSolution();
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException("Exception in projectFile (" + projectFile + ")", e);
				} catch (IllegalStateException e) {
					throw new IllegalStateException("Exception in projectFile (" + projectFile + ")", e);
				}
			} catch (IOException e) {
				throw new IllegalArgumentException("Could not read the projectFile (" + projectFile + ").", e);
			} finally {
				IOUtils.closeQuietly(bufferedReader);
			}
		}

		public class ProjectFileInputBuilder extends TxtInputBuilder {

			private Schedule schedule;
			private Project project;

			private int jobListSize;
			private int renewableLocalResourceSize;
			private int nonrenewableLocalResourceSize;

			public ProjectFileInputBuilder(Schedule schedule, Project project) {
				this.schedule = schedule;
				this.project = project;
			}

			public Solution<?> readSolution() throws IOException {
				readHeader();
				readResourceList();
				readProjectInformation();
				readPrecedenceRelations();
				readRequestDurations();
				readResourceAvailabilities();
				
				try {
					readMesJobAllocations();					
				} catch (Exception e) {
					useMesAllocations = false;
					logger.error(e.toString());
					logger.warn("Loading MES allocations faild. Back to standard mode.");
				}
				
				try {					
					readMesSchedulingSession();
					readMesJob2MesOperation();
					readMesRes2MesMachine();
				} catch (Exception e) {					
					logger.error(e.toString());
					logger.warn("Loading MES mappings faild. Export will not be possible.");
				}						
				
				detectPointlessSuccessor();
				return null; // Hack so the code can reuse read methods from
								// TxtInputBuilder
			}

			private void readHeader() throws IOException {
				readRegexConstantLine("\\*+");
				readStringValue("file with basedata            :");
				readStringValue("initial value random generator:");
				readRegexConstantLine("\\*+");
				int projects = readIntegerValue("projects                      :");
				if (projects != 1) {
					throw new IllegalArgumentException("The projects value (" + projects + ") should always be 1.");
				}
				jobListSize = readIntegerValue("jobs (incl. supersource/sink ):");
				@SuppressWarnings("unused")
				int horizon = readIntegerValue("horizon                       :");
				// Ignore horizon
			}

			private void readResourceList() throws IOException {
				readConstantLine("RESOURCES");
				int renewableResourceSize = readIntegerValue("- renewable                 :", "R");
				if (renewableResourceSize < globalResourceListSize) {
					throw new IllegalArgumentException("The renewableResourceSize (" + renewableResourceSize
							+ ") can not be less than globalResourceListSize (" + globalResourceListSize + ").");
				}
				renewableLocalResourceSize = renewableResourceSize - globalResourceListSize;
				nonrenewableLocalResourceSize = readIntegerValue("- nonrenewable              :", "N");
				int doublyConstrainedResourceSize = readIntegerValue("- doubly constrained        :", "D");
				if (doublyConstrainedResourceSize != 0) {
					throw new IllegalArgumentException("The doublyConstrainedResourceSize (" + doublyConstrainedResourceSize + ") should always be 0.");
				}
				List<LocalResource> localResourceList = new ArrayList<LocalResource>(globalResourceListSize + renewableLocalResourceSize
						+ nonrenewableLocalResourceSize);
				for (int i = 0; i < renewableLocalResourceSize; i++) {
					LocalResource localResource = new LocalResource();
					localResource.setId(resourceId);
					localResource.setProject(project);
					localResource.setRenewable(true);
					resourceId++;
					localResourceList.add(localResource);
				}
				for (int i = 0; i < nonrenewableLocalResourceSize; i++) {
					LocalResource localResource = new LocalResource();
					localResource.setId(resourceId);
					localResource.setProject(project);
					localResource.setRenewable(false);
					resourceId++;
					localResourceList.add(localResource);
				}
				project.setLocalResourceList(localResourceList);
				schedule.getResourceList().addAll(localResourceList);
				readRegexConstantLine("\\*+");
			}

			private void readProjectInformation() throws IOException {
				readConstantLine("PROJECT INFORMATION:");
				readConstantLine("pronr.  #jobs rel.date duedate tardcost  MPM-Time");
				String[] tokens = splitBySpacesOrTabs(readStringValue(), 6);
				if (Integer.parseInt(tokens[0]) != 1) {
					throw new IllegalArgumentException("The project information tokens (" + Arrays.toString(tokens) + ") index 0 should be 1.");
				}
				if (Integer.parseInt(tokens[1]) != jobListSize - 2) {
					throw new IllegalArgumentException("The project information tokens (" + Arrays.toString(tokens) + ") index 1 should be "
							+ (jobListSize - 2) + ".");
				}
				// Ignore releaseDate, dueDate, tardinessCost and mpmTime
				readRegexConstantLine("\\*+");
			}

			private void readPrecedenceRelations() throws IOException {
				readConstantLine("PRECEDENCE RELATIONS:");
				readConstantLine("jobnr.    #modes  #successors   successors");
				List<Job> jobList = new ArrayList<Job>(jobListSize);
				for (int i = 0; i < jobListSize; i++) {
					Job job = new Job();
					job.setId(jobId);
					job.setProject(project);
					if (i == 0) {
						job.setJobType(JobType.SOURCE);
					} else if (i == jobListSize - 1) {
						job.setJobType(JobType.SINK);
					} else {
						job.setJobType(JobType.STANDARD);
					}
					jobList.add(job);
					jobId++;
				}
				project.setJobList(jobList);
				schedule.getJobList().addAll(jobList);
				for (int i = 0; i < jobListSize; i++) {
					Job job = jobList.get(i);
					String[] tokens = splitBySpacesOrTabs(readStringValue());
					if (tokens.length < 3) {
						throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") should be at least 3 in length.");
					}
					if (Integer.parseInt(tokens[0]) != i + 1) {
						throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 0 should be " + (i + 1) + ".");
					}
					int executionModeListSize = Integer.parseInt(tokens[1]);
					List<ExecutionMode> executionModeList = new ArrayList<ExecutionMode>(executionModeListSize);
					for (int j = 0; j < executionModeListSize; j++) {
						ExecutionMode executionMode = new ExecutionMode();
						executionMode.setId(executionModeId);
						executionMode.setJob(job);
						executionModeList.add(executionMode);
						executionModeId++;
					}
					job.setExecutionModeList(executionModeList);
					schedule.getExecutionModeList().addAll(executionModeList);
					int successorJobListSize = Integer.parseInt(tokens[2]);
					if (tokens.length != 3 + successorJobListSize) {
						throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") should be " + (3 + successorJobListSize)
								+ " in length.");
					}
					List<Job> successorJobList = new ArrayList<Job>(successorJobListSize);
					for (int j = 0; j < successorJobListSize; j++) {
						int successorIndex = Integer.parseInt(tokens[3 + j]);
						Job successorJob = project.getJobList().get(successorIndex - 1);
						successorJobList.add(successorJob);
					}
					job.setSuccessorJobList(successorJobList);
				}
				readRegexConstantLine("\\*+");
			}

			private void readRequestDurations() throws IOException {
				readConstantLine("REQUESTS/DURATIONS:");
				splitBySpacesOrTabs(readStringValue());
				readRegexConstantLine("\\-+");
				int resourceSize = globalResourceListSize + renewableLocalResourceSize + nonrenewableLocalResourceSize;
				for (int i = 0; i < jobListSize; i++) {
					Job job = project.getJobList().get(i);
					int executionModeSize = job.getExecutionModeList().size();
					for (int j = 0; j < executionModeSize; j++) {
						ExecutionMode executionMode = job.getExecutionModeList().get(j);
						boolean first = j == 0;
						String[] tokens = splitBySpacesOrTabs(readStringValue(), (first ? 3 : 2) + resourceSize);
						if (first && Integer.parseInt(tokens[0]) != i + 1) {
							throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 0 should be " + (i + 1) + ".");
						}
						if (Integer.parseInt(tokens[first ? 1 : 0]) != j + 1) {
							throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index " + (first ? 1 : 0) + " should be "
									+ (j + 1) + ".");
						}
						int duration = Integer.parseInt(tokens[first ? 2 : 1]);
						executionMode.setDuration(duration);
						List<ResourceRequirement> resourceRequirementList = new ArrayList<ResourceRequirement>(resourceSize);
						for (int k = 0; k < resourceSize; k++) {
							int requirement = Integer.parseInt(tokens[(first ? 3 : 2) + k]);
							if (requirement != 0) {
								ResourceRequirement resourceRequirement = new ResourceRequirement();
								resourceRequirement.setId(resourceRequirementId);
								resourceRequirement.setExecutionMode(executionMode);
								Resource resource;
								if (k < globalResourceListSize) {
									resource = schedule.getResourceList().get(k);
								} else {
									resource = project.getLocalResourceList().get(k - globalResourceListSize);
								}
								resourceRequirement.setResource(resource);
								resourceRequirement.setRequirement(requirement);
								resourceRequirementList.add(resourceRequirement);
								resourceRequirementId++;
							}
						}
						executionMode.setResourceRequirementList(resourceRequirementList);
						schedule.getResourceRequirementList().addAll(resourceRequirementList);
					}
				}
				readRegexConstantLine("\\*+");
			}

			private void readResourceAvailabilities() throws IOException {
				readConstantLine("RESOURCEAVAILABILITIES:");
				splitBySpacesOrTabs(readStringValue());
				int resourceSize = globalResourceListSize + renewableLocalResourceSize + nonrenewableLocalResourceSize;
				String[] tokens = splitBySpacesOrTabs(readStringValue(), resourceSize);
				for (int i = 0; i < resourceSize; i++) {
					int capacity = Integer.parseInt(tokens[i]);
					if (i < globalResourceListSize) {
						// Overwritten by global resource
					} else {
						Resource resource = project.getLocalResourceList().get(i - globalResourceListSize);
						resource.setCapacity(capacity);
					}
				}
				readRegexConstantLine("\\*+");
			}

			private void readMesJobAllocations() throws IOException {
				readMesHeadingLine("JOBALLOCATIONS:");
				readMesHeadingLine("jobNumber    executionModeNumber    predecessorsDoneDate           delay");
				readRegexConstantLine("\\-+");

				for (int i = 0; i < jobListSize; i++) {
					String[] tokens = splitBySpacesOrTabs(readStringValue(), 4);

					int jobNumber = -1;
					try {
						jobNumber = Integer.parseInt(tokens[0]);
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 0 should be integer.");
					}

					int executionModeNumber = -1;
					try {
						executionModeNumber = Integer.parseInt(tokens[1]);
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 1 should be integer.");
					}

					int predecessorsDoneDate = -1;
					try {
						predecessorsDoneDate = Integer.parseInt(tokens[2]);
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 2 should be integer.");
					}

					int delay = -1;
					try {
						delay = Integer.parseInt(tokens[3]);
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 3 should be integer.");
					}
					if (delay < 0) {
						throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 3 should be positive integer.");
					}

					logger.debug("jobNumber {} has executionModeNumber {} and starts at {} with delay {}.", jobNumber, executionModeNumber,
							predecessorsDoneDate, delay);

					Job job;
					try {
						job = project.getJobList().get(jobNumber - 1);
					} catch (Exception e) {
						throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 0 should be existing job number.");
					}

					ExecutionMode executionMode;
					try {
						executionMode = job.getExecutionModeList().get(executionModeNumber - 1);
					} catch (Exception e) {
						throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 1 should be existing execution mode number.");
					}

					job.setInitialPredecessorsDoneDate(predecessorsDoneDate);
					job.setInitialExecutionMode(executionMode);
					job.setInitialDelay(delay);
				}
				readRegexConstantLine("\\*+");
			}

			private void readMesSchedulingSession() throws IOException {
				readMesHeadingLine("MESSCHEDULINGSESSION:");
				readMesHeadingLine("schedulingId           timeScale");
				readRegexConstantLine("\\-+");

				String[] tokens = splitBySpacesOrTabs(readStringValue(), 2);

				int schedulingId = -1;
				try {
					schedulingId = Integer.parseInt(tokens[0]);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 0 should be integer.");
				}

				int timeScale = -1;
				try {
					timeScale = Integer.parseInt(tokens[1]);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 0 should be integer.");
				}
				schedule.setMesSchedulingId(schedulingId);
				schedule.setMesTimeScale(timeScale);
				logger.debug("schedulingId {} uses timeScale {}.", schedulingId, timeScale);

				readRegexConstantLine("\\*+");
			}

			private void readMesJob2MesOperation() throws IOException {
				readMesHeadingLine("JOB2MESOPERATION:");
				readMesHeadingLine("jobNumber            operationId");
				readRegexConstantLine("\\-+");

				for (int i = 1; i < jobListSize - 1; i++) {
					String[] tokens = splitBySpacesOrTabs(readStringValue(), 2);

					int jobNumber = -1;
					try {
						jobNumber = Integer.parseInt(tokens[0]);
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 0 should be integer.");
					}

					int operationId = -1;
					try {
						operationId = Integer.parseInt(tokens[1]);
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 1 should be integer.");
					}
					logger.debug("jobNumber {} maps operationId {}.", jobNumber, operationId);

					Job job;
					try {
						job = project.getJobList().get(jobNumber - 1);
					} catch (Exception e) {
						throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 0 should be existing job number.");
					}

					job.setMesOperationId(operationId);
				}
				readRegexConstantLine("\\*+");
			}

			private void readMesRes2MesMachine() throws IOException {
				readMesHeadingLine("RESOURCE2MESMACHINE:");
				readMesHeadingLine("resNumber              machineId");
				readRegexConstantLine("\\-+");

				int resourceSize = globalResourceListSize + renewableLocalResourceSize + nonrenewableLocalResourceSize;
				for (int i = 0; i < resourceSize; i++) {
					String[] tokens = splitBySpacesOrTabs(readStringValue(), 2);

					int resNumber = -1;
					try {
						resNumber = Integer.parseInt(tokens[0]);
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 0 should be integer.");
					}

					int machineId = -1;
					try {
						machineId = Integer.parseInt(tokens[1]);
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("The tokens (" + Arrays.toString(tokens) + ") index 1 should be integer.");
					}
					schedule.getResourceList().get(i).setMesMachineId(machineId);
					logger.debug("resNumber {} maps machineId {}.", resNumber, machineId);
				}
				readRegexConstantLine("\\*+");
			}

			private void detectPointlessSuccessor() {
				for (Job baseJob : project.getJobList()) {
					Set<Job> baseSuccessorJobSet = new HashSet<Job>(baseJob.getSuccessorJobList());
					Set<Job> checkedSuccessorSet = new HashSet<Job>(project.getJobList().size());
					Queue<Job> uncheckedSuccessorQueue = new ArrayDeque<Job>(project.getJobList().size());
					for (Job baseSuccessorJob : baseJob.getSuccessorJobList()) {
						uncheckedSuccessorQueue.addAll(baseSuccessorJob.getSuccessorJobList());
					}
					while (!uncheckedSuccessorQueue.isEmpty()) {
						Job uncheckedJob = uncheckedSuccessorQueue.remove();
						if (checkedSuccessorSet.contains(uncheckedJob)) {
							continue;
						}
						if (baseSuccessorJobSet.contains(uncheckedJob)) {
							throw new IllegalStateException("The baseJob (" + baseJob + ") has a direct successor (" + uncheckedJob
									+ ") that is also an indirect successor. That's pointless.");
						}
						uncheckedSuccessorQueue.addAll(uncheckedJob.getSuccessorJobList());
					}
				}
			}
		}

		private void removePointlessExecutionModes() {
			// TODO iterate through schedule.getJobList(), find pointless
			// ExecutionModes
			// and delete them both from the job and from
			// schedule.getExecutionModeList()
		}

		private void createAllocationList() {
			List<Job> jobList = schedule.getJobList();
			List<Allocation> allocationList = new ArrayList<Allocation>(jobList.size());
			Map<Job, Allocation> jobToAllocationMap = new HashMap<Job, Allocation>(jobList.size());
			Map<Project, Allocation> projectToSourceAllocationMap = new HashMap<Project, Allocation>(projectListSize);
			Map<Project, Allocation> projectToSinkAllocationMap = new HashMap<Project, Allocation>(projectListSize);
			for (Job job : jobList) {
				Allocation allocation = new Allocation();
				allocation.setId(job.getId());
				allocation.setJob(job);
				allocation.setPredecessorAllocationList(new ArrayList<Allocation>(job.getSuccessorJobList().size()));
				allocation.setSuccessorAllocationList(new ArrayList<Allocation>(job.getSuccessorJobList().size()));
				// Uninitialized allocations take no time, but don't break the
				// predecessorsDoneDate cascade to sink.
				allocation.setPredecessorsDoneDate(job.getProject().getReleaseDate());
				if (job.getJobType() == JobType.SOURCE) {
					allocation.setDelay(0);
					if (job.getExecutionModeList().size() != 1) {
						throw new IllegalArgumentException("The job (" + job + ")'s executionModeList (" + job.getExecutionModeList()
								+ ") is expected to be a singleton.");
					}
					allocation.setExecutionMode(job.getExecutionModeList().get(0));
					projectToSourceAllocationMap.put(job.getProject(), allocation);
				} else if (job.getJobType() == JobType.SINK) {
					allocation.setDelay(0);
					if (job.getExecutionModeList().size() != 1) {
						throw new IllegalArgumentException("The job (" + job + ")'s executionModeList (" + job.getExecutionModeList()
								+ ") is expected to be a singleton.");
					}
					allocation.setExecutionMode(job.getExecutionModeList().get(0));
					projectToSinkAllocationMap.put(job.getProject(), allocation);
				}
				allocationList.add(allocation);
				jobToAllocationMap.put(job, allocation);
			}
			for (Allocation allocation : allocationList) {
				Job job = allocation.getJob();
				allocation.setSourceAllocation(projectToSourceAllocationMap.get(job.getProject()));
				allocation.setSinkAllocation(projectToSinkAllocationMap.get(job.getProject()));
				for (Job successorJob : job.getSuccessorJobList()) {
					Allocation successorAllocation = jobToAllocationMap.get(successorJob);
					allocation.getSuccessorAllocationList().add(successorAllocation);
					successorAllocation.getPredecessorAllocationList().add(allocation);
				}
			}
			for (Allocation sourceAllocation : projectToSourceAllocationMap.values()) {
				for (Allocation allocation : sourceAllocation.getSuccessorAllocationList()) {
					allocation.setPredecessorsDoneDate(sourceAllocation.getEndDate());
				}
			}
			schedule.setAllocationList(allocationList);
		}

		private void createMesAllocationList() {
			List<Job> jobList = schedule.getJobList();
			List<Allocation> allocationList = new ArrayList<Allocation>(jobList.size());
			Map<Job, Allocation> jobToAllocationMap = new HashMap<Job, Allocation>(jobList.size());
			Map<Project, Allocation> projectToSourceAllocationMap = new HashMap<Project, Allocation>(projectListSize);
			Map<Project, Allocation> projectToSinkAllocationMap = new HashMap<Project, Allocation>(projectListSize);
			for (Job job : jobList) {
				Allocation allocation = new Allocation();
				allocation.setId(job.getId());
				allocation.setJob(job);
				allocation.setPredecessorAllocationList(new ArrayList<Allocation>(job.getSuccessorJobList().size()));
				allocation.setSuccessorAllocationList(new ArrayList<Allocation>(job.getSuccessorJobList().size()));				
				allocation.setPredecessorsDoneDate(job.getInitialPredecessorsDoneDate());
				allocation.setExecutionMode(job.getInitialExecutionMode());
				allocation.setDelay(job.getInitialDelay());

				if (job.getJobType() == JobType.SOURCE) {
					if (job.getExecutionModeList().size() != 1) {
						throw new IllegalArgumentException("The job (" + job + ")'s executionModeList (" + job.getExecutionModeList()
								+ ") is expected to be a singleton.");
					}
					projectToSourceAllocationMap.put(job.getProject(), allocation);
				} else if (job.getJobType() == JobType.SINK) {
					if (job.getExecutionModeList().size() != 1) {
						throw new IllegalArgumentException("The job (" + job + ")'s executionModeList (" + job.getExecutionModeList()
								+ ") is expected to be a singleton.");
					}
					projectToSinkAllocationMap.put(job.getProject(), allocation);
				}
				allocationList.add(allocation);
				jobToAllocationMap.put(job, allocation);

			}
			for (Allocation allocation : allocationList) {

				Job job = allocation.getJob();
				allocation.setSourceAllocation(projectToSourceAllocationMap.get(job.getProject()));
				allocation.setSinkAllocation(projectToSinkAllocationMap.get(job.getProject()));
				for (Job successorJob : job.getSuccessorJobList()) {
					Allocation successorAllocation = jobToAllocationMap.get(successorJob);
					allocation.getSuccessorAllocationList().add(successorAllocation);
					successorAllocation.getPredecessorAllocationList().add(allocation);
				}
			}
			schedule.setAllocationList(allocationList);
		}
	}
}
