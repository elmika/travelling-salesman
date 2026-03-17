import { useRef } from 'react';
import type { Point, Route } from '../api/types';
import { useCanvasRenderer } from '../hooks/useCanvasRenderer';

interface RouteCanvasProps {
  points: Point[];
  route: Route | null;
}

export function RouteCanvas({ points, route }: RouteCanvasProps) {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  useCanvasRenderer(canvasRef, points, route);

  return (
    <div className="canvas-area">
      <canvas ref={canvasRef} style={{ display: 'block' }} />
    </div>
  );
}
