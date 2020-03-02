package nl.miystengine.client;

public class DeltaTime
{
	private float ticksPerSecond;
    private double lastHRTime;
    public int elapsedTicks;
    public float renderPartialTicks;
    public static float timerSpeed = 10F;
    public float elapsedPartialTicks;
    private long lastSyncSysClock;
    private long lastSyncHRClock;
    private long f;
    private double timeSyncAdjustment = 1.0D;

    
    public DeltaTime(float f)
    {
        this.ticksPerSecond = f;
        this.lastSyncSysClock = MiystEngine.getSystemTime();
        this.lastSyncHRClock = System.nanoTime() / 1000000L;
    }

    /**
     * Updates all fields of the Timer using the current time
     */
    public void updateTimer()
    {
        long var1 = MiystEngine.getSystemTime();
        long var3 = var1 - this.lastSyncSysClock;
        long var5 = System.nanoTime() / 1000000L;
        double var7 = (double)var5 / 1000.0D;

        if (var3 <= 1000L && var3 >= 0L)
        {
            this.f += var3;

            if (this.f > 1000L)
            {
                long var9 = var5 - this.lastSyncHRClock;
                double var11 = (double)this.f / (double)var9;
                this.timeSyncAdjustment += (var11 - this.timeSyncAdjustment) * 0.20000000298023224D;
                this.lastSyncHRClock = var5;
                this.f = 0L;
            }

            if (this.f < 0L)
            {
                this.lastSyncHRClock = var5;
            }
        }
        else
        {
            this.lastHRTime = var7;
        }

        this.lastSyncSysClock = var1;
        double var13 = (var7 - this.lastHRTime) * this.timeSyncAdjustment;
        this.lastHRTime = var7;

        if (var13 < 0.0D)
        {
            var13 = 0.0D;
        }

        if (var13 > 1.0D)
        {
            var13 = 1.0D;
        }

        this.elapsedPartialTicks = (float)((double)this.elapsedPartialTicks + var13 * (double)this.timerSpeed * (double)this.ticksPerSecond);
        this.elapsedTicks = (int)this.elapsedPartialTicks;
        this.elapsedPartialTicks -= (float)this.elapsedTicks;

        if (this.elapsedTicks > 2)
        {
            this.elapsedTicks = 2;
        }

        this.renderPartialTicks = this.elapsedPartialTicks;
   }
    

}
