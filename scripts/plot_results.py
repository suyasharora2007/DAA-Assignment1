import csv, math
from pathlib import Path
import matplotlib.pyplot as plt
ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'plots'; OUT.mkdir(exist_ok=True)
with (ROOT/'results.csv').open(newline='') as f:
    rows = list(csv.DictReader(f))
algorithms=['MergeSort','QuickSort','QuickSelect']
inputs=['random','sorted','duplicates']
def plot(metric, ylabel, title, filename):
    plt.figure(figsize=(8,5))
    for alg in algorithms:
        for inp in inputs:
            s=sorted([r for r in rows if r['algorithm']==alg and r['input']==inp], key=lambda r:int(r['n']))
            if not s: continue
            x=[int(r['n']) for r in s]
            if metric=='time': y=[float(r['time_ms']) for r in s]
            elif metric=='depth': y=[int(r['max_depth']) for r in s]
            else:
                y=[]
                for r,n in zip(s,x):
                    c=int(r['comparisons']); y.append(c/n if alg=='QuickSelect' else c/(n*math.log2(n)))
            plt.plot(x,y,marker='o',label=f'{alg} - {inp}')
    plt.xlabel('n'); plt.ylabel(ylabel); plt.title(title); plt.xscale('log')
    plt.grid(True,alpha=0.25); plt.legend(fontsize=7,ncol=2); plt.tight_layout(); plt.savefig(OUT/filename,dpi=180); plt.close()
plot('time','Time (ms)','Time vs n','time_vs_n.png')
plot('depth','Maximum recursion depth','Max recursion depth vs n','depth_vs_n.png')
plot('ratio','Comparison ratio','Ratio vs n','ratio_vs_n.png')
print('Created combined plots in',OUT)
