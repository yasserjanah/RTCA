- Each added feature, bug fix or code change should be represented as a pull request.
- That should happen through branching as there is **no push to the main branch**.

- Following a naming convention when create a branch name. example create-full-project-branch
```bash
$ git clone https://github.com/yasserjanah/RTCA.git
$ git checkout -b create-full-project-branch
$ git add file1.py file2.py
$ git commit -m "commit message"
$ git push origin create-full-project-branch
```
- Wait for the pull request to be reviewed and merged.
- Always write a clear log message for your commits. One-line messages are fine for small changes, but bigger changes should look like this:
```bash
$ git commit -m "A brief summary of the commit
> 
> A paragraph describing what changed and its impact."
```

- each time a new pull request is merged into the main branch, you should execute **git pull**
```bash
$ git checkout main
$ git pull
```
