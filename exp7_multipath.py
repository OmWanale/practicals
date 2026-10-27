
# Exp 7: Multipath fading - sum of many reflected paths as receiver moves

import numpy as np
import matplotlib.pyplot as plt

np.random.seed(1)

f, c = 900e6, 3e8
lam = c / f                         # 900 MHz carrier
d = np.linspace(0, 5, 1000)         # receiver moves 5 m
P = 20                              # number of paths

amp = np.random.rand(P)
ph = 2 * np.pi * np.random.rand(P)
ang = 2 * np.pi * np.random.rand(P)

# Each path has a different phase shift depending on arrival angle
h = sum(
    amp[k] * np.exp(
        1j * (ph[k] + 2 * np.pi * d / lam * np.cos(ang[k]))
    )
    for k in range(P)
)

# Calculate signal level relative to its mean
rssi = 20 * np.log10(np.abs(h) / np.abs(h).mean())

# Print results BEFORE opening the graph
print("Minimum signal level: %.2f dB" % rssi.min())
print("Fades below -10 dB: %.1f %%" % (100 * (rssi < -10).mean()))

# Plot the multipath fading graph
plt.figure(figsize=(9, 4))
plt.plot(d, rssi, label="Signal Level")
plt.axhline(
    -10,
    color="r",
    linestyle="--",
    label="Deep Fade (-10 dB)"
)

plt.xlabel("Distance moved (m)")
plt.ylabel("Signal Level (dB)")
plt.grid(alpha=0.3)
plt.legend()
plt.title("Multipath Fading: 20 Paths, 900 MHz")
plt.tight_layout()

# Save graph and display it
plt.savefig("exp7_multipath.png", dpi=150)
plt.show()
