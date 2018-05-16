

#If you are using external usb sound card:

On Raspberry jessie

-sudo nano /etc/asound.conf (with this command you would be able to modify or create the file not exist, both works)

and Enter " pcm.!default { type hw card 1

} ctl.!default { type hw card 1

}

"

(might need to copy past it in ~/.asoundrc also)

reboot

**On Raspberry wheezy -sudo nano /etc/mo/modprobe.d/alsa-base.conf and change #options snd-usb-audio index=0 (might be =-2) to options snd-usb-audio index=0

Finally

-reboot
