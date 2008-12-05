package org.codehaus.mojo.wagon;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.wagon.ConnectionException;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.WagonException;

public abstract class AbstractCopyMojo
    extends AbstractDoubleWagonMojo
{

    protected abstract void copy(  Wagon src, Wagon target ) 
        throws IOException, WagonException;
    
    public void execute()
        throws MojoExecutionException
    {
        Wagon srcWagon = null;
        Wagon targetWagon = null;

        try
        {
            srcWagon = AbstractWagonMojo.createWagon( sourceId, source, wagonManager, settings, this.getLog() );
            targetWagon = AbstractWagonMojo.createWagon( targetId, target, wagonManager, settings, this.getLog() );
            copy( srcWagon, targetWagon );
        }
        catch ( IOException iox )
        {
            throw new MojoExecutionException( "Error during performing repository copy", iox );
        }
        catch ( WagonException e )
        {
            throw new MojoExecutionException( "Error during performing repository copy", e );
        }
        finally
        {
            try
            {
                if ( srcWagon != null )
                {
                    srcWagon.disconnect();
                }
            }
            catch ( ConnectionException e )
            {
                getLog().debug( "Error disconnecting wagon - ignored", e );
            }

            try
            {
                if ( targetWagon != null )
                {
                    targetWagon.disconnect();
                }
            }
            catch ( ConnectionException e )
            {
                getLog().debug( "Error disconnecting wagon - ignored", e );
            }
        }

    }

}