#!/usr/bin/env

git checkout gh-pages
git reset --hard origin/main
mv ./html/build/dist/* .
git rm -rf --cache .
git add .
git commit -m "deploy"
git push origin gh-pages --force