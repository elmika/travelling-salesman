import type { BenchmarkResult } from '../api/types';

interface BenchmarkTableProps {
  results: BenchmarkResult[];
}

export function BenchmarkTable({ results }: BenchmarkTableProps) {
  return (
    <div className="benchmark-area">
      {results.length === 0 ? (
        <p style={{ color: '#9e9e9e', fontStyle: 'italic', fontSize: 13 }}>
          No benchmark results yet.
        </p>
      ) : (
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 13 }}>
          <thead>
            <tr style={{ borderBottom: '1px solid #e0e0e0' }}>
              <th style={{ textAlign: 'left', padding: '4px 8px' }}>Rank</th>
              <th style={{ textAlign: 'left', padding: '4px 8px' }}>Label</th>
              <th style={{ textAlign: 'right', padding: '4px 8px' }}>Distance</th>
              <th style={{ textAlign: 'right', padding: '4px 8px' }}>Gap</th>
              <th style={{ textAlign: 'right', padding: '4px 8px' }}>Duration (ms)</th>
            </tr>
          </thead>
          <tbody>
            {results.map((r) => (
              <tr
                key={r.rank}
                style={{
                  background: r.rank === 1 ? '#e8f5e9' : undefined,
                  borderBottom: '1px solid #f0f0f0',
                }}
              >
                <td style={{ padding: '4px 8px' }}>{r.rank}</td>
                <td style={{ padding: '4px 8px' }}>{r.label}</td>
                <td style={{ textAlign: 'right', padding: '4px 8px' }}>
                  {r.distance.toFixed(2)}
                </td>
                <td style={{ textAlign: 'right', padding: '4px 8px' }}>
                  {r.gapPercent.toFixed(1)}%
                </td>
                <td style={{ textAlign: 'right', padding: '4px 8px' }}>{r.durationMs}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
