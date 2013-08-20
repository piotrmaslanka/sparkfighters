package com.sparkfighters.client.game.ultis;

import net.java.truecommons.key.spec.common.AesKeyStrength;
import net.java.truevfs.access.TArchiveDetector;
import net.java.truevfs.access.TConfig;
import net.java.truevfs.driver.zip.raes.SafeZipRaesDriver;
import net.java.truevfs.driver.zip.raes.crypto.RaesKeyException;
import net.java.truevfs.driver.zip.raes.crypto.RaesParameters;
import net.java.truevfs.driver.zip.raes.crypto.Type0RaesParameters;
import net.java.truevfs.kernel.spec.FsController;
import net.java.truevfs.kernel.spec.FsModel;


public class VFS 
{	
	public static void setDetector()
	{		
		String extensions = "dat";
		String pass="sf40Game19Admin370only";
		char[] password = pass.toCharArray();
		
		TConfig.current().setArchiveDetector(VFS.newArchiveDetector(extensions, password));
	}

    public static TArchiveDetector newArchiveDetector(String extensions, char[] password) 
    {
        return new TArchiveDetector(extensions, new CustomZipRaesDriver1(password));
    }

    private static final class CustomZipRaesDriver1 extends SafeZipRaesDriver 
    {
        final RaesParameters param;

        CustomZipRaesDriver1(char[] password) 
        {
            param = new CustomRaesParameters(password);
        }

        protected RaesParameters raesParameters(FsModel model) 
        {
            return param;
        }

        public FsController decorate(FsController controller) 
        {
            return controller;
        }
    }

    private static final class CustomRaesParameters implements Type0RaesParameters 
    {

        final char[] password;

        CustomRaesParameters(final char[] password) 
        {
            this.password = password.clone();
        }

        public char[] getPasswordForWriting()
        throws RaesKeyException 
        {
            return password.clone();
        }

        public char[] getPasswordForReading(boolean invalid)
        throws RaesKeyException 
        {
            if (invalid) throw new RaesKeyException("Invalid password!");
            return password.clone();
        }

        public AesKeyStrength getKeyStrength()
        throws RaesKeyException
        {
            return AesKeyStrength.BITS_256;
        }

        public void setKeyStrength(AesKeyStrength keyStrength)
        throws RaesKeyException 
        {
            assert AesKeyStrength.BITS_256 == keyStrength;
        }
    } 
}
