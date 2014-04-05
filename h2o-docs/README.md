# H<sub>2</sub>O Documentation

## Installing Sphinx on OSX Mavericks 10.9.2

Xcode 5.1 seems to have broken CPython behavior on Mavericks. Here's how to install Sphinx:

Setting up easy_install, pip:

    curl -O http://python-distribute.org/distribute_setup.py
    sudo python distribute_setup.py
    sudo rm distribute_setup.py
    sudo easy_install pip

Setting up Sphinx:

    export CFLAGS=-Qunused-arguments
    export CPPFLAGS=-Qunused-arguments
    sudo -E pip install Sphinx

