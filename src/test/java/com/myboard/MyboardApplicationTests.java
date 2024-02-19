package com.myboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyboardApplicationTests {
    static class Node{
        int v; //간선 (연결된 노드 번호)
        int cost; //가중치

        public Node(int v, int cost) {
            this.v = v;
            this.cost = cost;
        }
    }
    
	//각 노드에 연결되어 있는 노드에 대한 정보를 담는 리스트
    //한 노드에 여러개 노드가 연결되어 있을 수 있으므로 ArrayList<Node>
    //graph 의 인덱스가 노드 번호이다.
    //즉, i번째 인덱스에 있는 값인 ArrayList<Node>는 i번 노드에 연결된 노드들의 값과 
    // 그 노드에 연결된 간선 가중치 값이다.
	static ArrayList<Node>[] graph;
	
	//방문한 적이 있는지 체크하는 목적의 리스트
	static boolean[] visit;
	//최단 거리 테이블
	static int[] dist;

	@Test
	public void mainFunc() throws IOException {
		//노드, 간선 갯수 입력 (ex: 5 6)
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    StringTokenizer st = new StringTokenizer(br.readLine());
	
	    int v = Integer.parseInt(st.nextToken()); //노드
	    int e = Integer.parseInt(st.nextToken()); //간선
	    
	    //시작 노드 입력
	    int k = Integer.parseInt(br.readLine()); //ex 1
	
	    //배열 사이즈들 초기화
	    //v+1 인 이유는 0번째 인덱스는 없는 셈 치고 1부터 카운팅해야 편하기때문으로 추정
	    graph = new ArrayList[v + 1]; 
	    dist = new int[v + 1];
	    visit = new boolean[v + 1];
	
	    //graph 와 dist 초기화 (1부터 카운팅)
	    for (int i = 1; i <= v; i++) {
	        graph[i] = new ArrayList<>();
	        dist[i] = Integer.MAX_VALUE; //최대값으로 초기화, 최단거리를 찾기 위함.
	    }
	
	    for (int i = 0; i < e; i++) {
	        // u -> v 로 가는 가중치 w가 주어진다.
	        st = new StringTokenizer(br.readLine());
	        int inputU = Integer.parseInt(st.nextToken());
	        int inputV = Integer.parseInt(st.nextToken());
	        int inputW = Integer.parseInt(st.nextToken());
	
	        //u 노드가 v 노드로 가는데에 가중치 w
	        graph[inputU].add(new Node(inputV, inputW));
	    }
	
	    //다익스트라 알고리즘 수행
	    dijkstra(k);
	
	    for (int i = 1; i <= v; i++) {
	        System.out.println(dist[i] == Integer.MAX_VALUE ? "INF" : dist[i]);
	    }
	}

	static void dijkstra(int start) {
	    //우선 순위 큐 사용, 가중치를 기준으로 오름차순한다. (초기화)
	    PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> o1.cost - o2.cost);
	    
	    //시작 노드에 대해서 초기화
	    q.add(new Node(start, 0));
	    dist[start] = 0;
	
	    while (!q.isEmpty()) {
	        //현재 최단 거리가 가장 짧은 노드를 꺼내서 방문 처리 한다.
	        Node now = q.poll();
	
	        if (!visit[now.v]) {
	            visit[now.v] = true;
	        }
	
	        //현재 노드에 연결 된 
	        for (Node next : graph[now.v]) {
	
	            //방문하지 않았고, 현재 노드를 거쳐서 다른 노드로 이동하는 거리가 더 짧을 경우
	        	//dist[next.v] 현재 for문으로 탐색중인 노드 까지 cost 합 (이전 까지 최단거리로 판단되었던)
	        	//now.cost + next.cost = 현재 탐색중인 경로의 cost
	            if (!visit[next.v] && dist[next.v] > now.cost + next.cost) {
	                dist[next.v] = now.cost + next.cost; // 최단거리 최신화
	                q.add(new Node(next.v, dist[next.v])); //
	                System.out.println("next.v: " + next.v);
	            }
	        }
	    }
	}
	
}




