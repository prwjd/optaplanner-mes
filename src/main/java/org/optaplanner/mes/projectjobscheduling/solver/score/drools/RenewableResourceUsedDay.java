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

package org.optaplanner.mes.projectjobscheduling.solver.score.drools;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.optaplanner.mes.projectjobscheduling.domain.resource.Resource;

@SuppressWarnings("serial")
public class RenewableResourceUsedDay implements Serializable {

    private final Resource resource;
    private final int usedDay;

    public RenewableResourceUsedDay(Resource resource, int usedDay) {
        this.resource = resource;
        this.usedDay = usedDay;
    }

    public Resource getResource() {
        return resource;
    }

    public int getUsedDay() {
        return usedDay;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof RenewableResourceUsedDay) {
            RenewableResourceUsedDay other = (RenewableResourceUsedDay) o;
            return new EqualsBuilder()
                    .append(resource, other.resource)
                    .append(usedDay, other.usedDay)
                    .isEquals();
        } else {
            return false;
        }
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(resource)
                .append(usedDay)
                .toHashCode();
    }

    @Override
    public String toString() {
        return resource + " on " + usedDay;
    }

    public int getResourceCapacity() {
        return resource.getCapacity();
    }

}
