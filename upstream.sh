#!/bin/bash

# Script to keep in sync with the upstream fork

git pull
git remote add upstream https://github.com/tom2208/ChestCleaner.git

# git fetch upstream master
# git merge upstream/master

git pull -r upstream master
